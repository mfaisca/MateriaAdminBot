package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Ace extends _Default{
	public Ace() {
		super("Ace", null);
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{4725, 4894};
				break;
			case HP:
				ids = new int[]{4732, 4895};
				break;
			case S1:
				ids = new int[]{4722, 4724, 4723};
				break;
			case S2:
				ids = new int[]{4729, 4731, 4730};
				break;
			case EX:
				ids = new int[]{5207};
				break;
			case AA:
				ids = new int[]{4693};
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