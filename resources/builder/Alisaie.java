package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Alisaie extends _Default{
	public Alisaie() {
		super("Alisaie", "ali");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{9208};
				break;
			case HP:
				ids = new int[]{4150};
				break;
			case S1:
				ids = new int[]{4138, 4140, 4139};
				break;
			case S2:
				ids = new int[]{4144, 4146, 4145};
				break;
			case EX:
				ids = new int[]{8857};
				break;
			case AA:
				ids = new int[]{4342};
				break;
			case LD:
				ids = new int[]{};
				break;
			case BT:
				ids = new int[]{};
				break;
			case CA:
				ids = new int[]{};
				break;
		}
		if(ids.length == 0)
			return super.getAbility(type);
		else
			return IntStream.of(ids).boxed().map(i -> this.getSpecificAbility(i)).collect(Collectors.toList());
	}
}