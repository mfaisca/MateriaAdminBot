package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Desch extends _Default{
	public Desch() {
		super("Desch", "desh", "desh");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{9004};
				break;
			case HP:
				ids = new int[]{9005};
				break;
			case S1:
				ids = new int[]{8498};
				break;
			case S2:
				ids = new int[]{8502};
				break;
			case EX:
				ids = new int[]{9003};
				break;
			case AA:
				ids = new int[]{8507};
				break;
			case LD:
				ids = new int[]{9006};
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